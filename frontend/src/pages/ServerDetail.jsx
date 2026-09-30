import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getServerById } from '../services/serverService';
import StatusBadge from '../components/StatusBadge';
import SeverityBadge from '../components/SeverityBadge';
import EnvironmentBadge from '../components/EnvironmentBadge';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';

function ServerDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [server, setServer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchServer = async () => {
      try {
        const data = await getServerById(id);
        setServer(data);
        setError(null);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to fetch server details');
      } finally {
        setLoading(false);
      }
    };
    fetchServer();
  }, [id]);

  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage message={error} />;
  if (!server) return null;

  return (
    <div>
      <div className="page-header">
        <h2 className="page-title">Server Details: {server.hostname}</h2>
        <button className="btn btn-secondary" onClick={() => navigate('/servers')}>Back to Servers</button>
      </div>

      <div className="card">
        <h3>Server Information</h3>
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginTop: '15px' }}>
          <div><strong>Hostname:</strong> {server.hostname}</div>
          <div><strong>IP Address:</strong> {server.ipAddress}</div>
          <div><strong>OS:</strong> {server.os}</div>
          <div><strong>Environment:</strong> <EnvironmentBadge environment={server.environment} /></div>
          <div><strong>Owner Team:</strong> {server.ownerTeam}</div>
          <div><strong>Status:</strong> <StatusBadge status={server.status} /></div>
          <div><strong>Last Checked:</strong> {server.lastChecked ? new Date(server.lastChecked).toLocaleString() : 'N/A'}</div>
        </div>
      </div>

      <div className="card mt-4">
        <h3>Patch History</h3>
        <div className="table-container mt-4">
          <table>
            <thead>
              <tr>
                <th>Patch Name</th>
                <th>Identifier</th>
                <th>Severity</th>
                <th>Status</th>
                <th>Event Date</th>
                <th>Failure Reason</th>
                <th>Remarks</th>
              </tr>
            </thead>
            <tbody>
              {server.patchHistory && server.patchHistory.length > 0 ? (
                server.patchHistory.map((event, index) => (
                  <tr key={index}>
                    <td>{event.patchName}</td>
                    <td>{event.patchIdentifier}</td>
                    <td><SeverityBadge severity={event.severity} /></td>
                    <td><StatusBadge status={event.status} /></td>
                    <td>{new Date(event.eventDate).toLocaleString()}</td>
                    <td>{event.failureReason || '-'}</td>
                    <td>{event.remarks || '-'}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" style={{ textAlign: 'center' }}>No patch history found for this server.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default ServerDetail;
