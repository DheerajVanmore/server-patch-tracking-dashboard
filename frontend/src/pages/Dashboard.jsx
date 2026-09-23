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
        <SummaryCard title="Total Servers" value={data.totalServers} color="#1a73e8" />
        <SummaryCard title="Patched" value={data.patched} color="#34a853" />
        <SummaryCard title="Pending" value={data.pending} color="#fbbc04" />
        <SummaryCard title="Failed" value={data.failed} color="#ea4335" />
        <SummaryCard title="Exempted" value={data.exempted} color="#4285f4" />
        <SummaryCard 
          title="Compliance %" 
          value={`${data.compliancePercentage.toFixed(1)}%`} 
          color={data.compliancePercentage >= 90 ? '#34a853' : data.compliancePercentage >= 75 ? '#fbbc04' : '#ea4335'} 
        />
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
