import { useState, useEffect } from 'react';
import { getAlerts } from '../services/alertService';
import SeverityBadge from '../components/SeverityBadge';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';

function Alerts() {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchAlerts = async () => {
      try {
        const data = await getAlerts();
        // Sort by severity (CRITICAL first, then HIGH, etc.)
        const severityOrder = { 'CRITICAL': 1, 'HIGH': 2, 'MEDIUM': 3, 'LOW': 4 };
        const sorted = data.sort((a, b) => (severityOrder[a.severity] || 5) - (severityOrder[b.severity] || 5));
        
        setAlerts(sorted);
        setError(null);
      } catch (err) {
        setError(err.response?.data?.message || 'Failed to fetch alerts');
      } finally {
        setLoading(false);
      }
    };
    fetchAlerts();
  }, []);

  if (loading) return <LoadingSpinner />;
  if (error) return <ErrorMessage message={error} />;

  return (
    <div>
      <div className="page-header">
        <h2 className="page-title">System Alerts</h2>
      </div>

      {alerts.length === 0 ? (
        <EmptyState message="No active alerts." />
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
          {alerts.map(alert => (
            <div key={alert.id} className={`alert-card ${alert.severity === 'CRITICAL' ? 'critical' : alert.severity === 'HIGH' ? 'high' : alert.severity === 'MEDIUM' ? 'medium' : ''}`}>
              <div className="flex justify-between items-center mb-2">
                <div className="flex gap-3 items-center">
                  <SeverityBadge severity={alert.severity} />
                  <span style={{ fontWeight: 600 }}>{alert.alertType}</span>
                </div>
                <span className="text-secondary">{new Date(alert.createdAt).toLocaleString()}</span>
              </div>
              <p style={{ margin: '10px 0' }}>{alert.message}</p>
              <div className="text-secondary" style={{ fontSize: '0.9rem' }}>
                {alert.server && <span><strong>Server:</strong> {alert.server.hostname} </span>}
                {alert.patch && <span> | <strong>Patch:</strong> {alert.patch.patchName}</span>}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default Alerts;
