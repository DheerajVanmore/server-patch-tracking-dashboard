import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getSummary } from '../services/dashboardService';
import SummaryCard from '../components/SummaryCard';
import StatusBadge from '../components/StatusBadge';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';

function Dashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const result = await getSummary();
        setData(result);
        setError(null);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to fetch dashboard data');
      } finally {
        setLoading(false);
      }
    };
    fetchSummary();
  }, []);

  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage message={error} />;
  if (!data) return null;

  return (
    <div>
      <div className="page-header">
        <h2 className="page-title">Dashboard</h2>
      </div>

      <div className="summary-grid">
        <SummaryCard title="Total Servers" value={data.totalServers} color="var(--primary-color)" />
        <SummaryCard title="Patched" value={data.patched} color="var(--success-color)" />
        <SummaryCard title="Pending" value={data.pending} color="var(--warning-color)" />
        <SummaryCard title="Failed" value={data.failed} color="var(--danger-color)" />
        <SummaryCard title="Exempted" value={data.exempted} color="var(--info-color)" />
        <div className="summary-card" style={{ borderLeft: `4px solid ${data.compliancePercentage >= 90 ? 'var(--success-color)' : data.compliancePercentage >= 75 ? 'var(--warning-color)' : 'var(--danger-color)'}` }}>
          <h3 style={{ color: 'var(--text-secondary)' }}>Compliance %</h3>
          <div className="value" style={{ color: 'var(--text-primary)', marginBottom: '8px' }}>{`${data.compliancePercentage.toFixed(1)}%`}</div>
          <div style={{ width: '100%', backgroundColor: 'var(--border-color)', height: '6px', borderRadius: '3px', overflow: 'hidden' }}>
            <div style={{ 
              width: `${data.compliancePercentage}%`, 
              backgroundColor: data.compliancePercentage >= 90 ? 'var(--success-color)' : data.compliancePercentage >= 75 ? 'var(--warning-color)' : 'var(--danger-color)',
              height: '100%' 
            }}></div>
          </div>
        </div>
      </div>

      <div className="card">
        <h3>Recent Patch Events</h3>
        <div className="table-container mt-4">
          <table>
            <thead>
              <tr>
                <th>Server</th>
                <th>Patch</th>
                <th>Status</th>
                <th>Event Date</th>
              </tr>
            </thead>
            <tbody>
              {data.recentEvents?.length > 0 ? (
                data.recentEvents.map(event => (
                  <tr key={event.id}>
                    <td>{event.server.hostname}</td>
                    <td>{event.patch.patchName}</td>
                    <td><StatusBadge status={event.status} /></td>
                    <td>{new Date(event.eventDate).toLocaleString()}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" style={{ textAlign: 'center' }}>No recent events</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      <div className="card">
        <div className="flex justify-between items-center mb-4">
          <h3>Recent Alerts</h3>
          <Link to="/alerts" className="btn btn-secondary">View All</Link>
        </div>
        <p className="text-secondary">Navigate to the Alerts page to view system warnings and critical notifications.</p>
      </div>
    </div>
  );
}

export default Dashboard;
