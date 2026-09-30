import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getServers, createServer, updateServer, deleteServer } from '../services/serverService';
import StatusBadge from '../components/StatusBadge';
import EnvironmentBadge from '../components/EnvironmentBadge';
import SearchBar from '../components/SearchBar';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import ConfirmDialog from '../components/ConfirmDialog';

function Servers() {
  const [servers, setServers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Filters
  const [search, setSearch] = useState('');
  const [environment, setEnvironment] = useState('');
  const [status, setStatus] = useState('');
  
  // Modals
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [currentServer, setCurrentServer] = useState(null);
  
  // Form State
  const [formData, setFormData] = useState({
    hostname: '', ipAddress: '', os: '', environment: 'Production', ownerTeam: '', status: 'ACTIVE'
  });

  const fetchServers = async () => {
    setLoading(true);
    try {
      const params = {};
      if (search) params.search = search;
      if (environment) params.environment = environment;
      if (status) params.status = status;
      
      const data = await getServers(params);
      setServers(data);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch servers');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      fetchServers();
    }, 300);
    return () => clearTimeout(delayDebounceFn);
  }, [search, environment, status]);

  const handleOpenForm = (server = null) => {
    if (server) {
      setCurrentServer(server);
      setFormData({
        hostname: server.hostname,
        ipAddress: server.ipAddress,
        os: server.os,
        environment: server.environment,
        ownerTeam: server.ownerTeam,
        status: server.status
      });
    } else {
      setCurrentServer(null);
      setFormData({
        hostname: '', ipAddress: '', os: '', environment: 'Production', ownerTeam: '', status: 'ACTIVE'
      });
    }
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
    setCurrentServer(null);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (currentServer) {
        await updateServer(currentServer.id, formData);
      } else {
        await createServer(formData);
      }
      handleCloseForm();
      fetchServers();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to save server');
    }
  };

  const handleDeleteClick = (server) => {
    setCurrentServer(server);
    setIsConfirmOpen(true);
  };

  const confirmDelete = async () => {
    try {
      await deleteServer(currentServer.id);
      setIsConfirmOpen(false);
      setCurrentServer(null);
      fetchServers();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to delete server');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h2 className="page-title">Servers</h2>
        <button className="btn btn-primary" onClick={() => handleOpenForm()}>Add Server</button>
      </div>

      <div className="filters-bar">
        <div className="filter-item">
          <SearchBar value={search} onChange={setSearch} placeholder="Search by hostname or IP..." />
        </div>
        <div className="filter-item">
          <select className="form-control" value={environment} onChange={(e) => setEnvironment(e.target.value)}>
            <option value="">All Environments</option>
            <option value="Production">Production</option>
            <option value="Staging">Staging</option>
            <option value="Development">Development</option>
          </select>
        </div>
        <div className="filter-item">
          <select className="form-control" value={status} onChange={(e) => setStatus(e.target.value)}>
            <option value="">All Statuses</option>
            <option value="ACTIVE">ACTIVE</option>
            <option value="MAINTENANCE">MAINTENANCE</option>
            <option value="OFFLINE">OFFLINE</option>
          </select>
        </div>
      </div>

      {error && <ErrorMessage message={error} />}

      <div className="card">
        {loading ? (
          <LoadingSpinner />
        ) : (
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Hostname</th>
                  <th>IP Address</th>
                  <th>OS</th>
                  <th>Environment</th>
                  <th>Owner Team</th>
                  <th>Status</th>
                  <th>Last Checked</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {servers.length > 0 ? (
                  servers.map(server => (
                    <tr key={server.id}>
                      <td><Link to={`/servers/${server.id}`} style={{color: 'var(--primary-color)', fontWeight: 500}}>{server.hostname}</Link></td>
                      <td>{server.ipAddress}</td>
                      <td>{server.os}</td>
                      <td><EnvironmentBadge environment={server.environment} /></td>
                      <td>{server.ownerTeam}</td>
                      <td><StatusBadge status={server.status} /></td>
                      <td>{server.lastChecked ? new Date(server.lastChecked).toLocaleString() : 'N/A'}</td>
                      <td>
                        <div className="flex gap-2">
                          <Link to={`/servers/${server.id}`} className="btn btn-secondary">View</Link>
                          <button className="btn btn-secondary" onClick={() => handleOpenForm(server)}>Edit</button>
                          <button className="btn btn-danger" onClick={() => handleDeleteClick(server)}>Delete</button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="8" style={{ textAlign: 'center', padding: '20px' }}>No servers found.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Server Form Modal */}
      {isFormOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2 className="modal-title">{currentServer ? 'Edit Server' : 'Add Server'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Hostname *</label>
                <input type="text" name="hostname" className="form-control" value={formData.hostname} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>IP Address *</label>
                <input type="text" name="ipAddress" className="form-control" value={formData.ipAddress} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>OS *</label>
                <input type="text" name="os" className="form-control" value={formData.os} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>Environment</label>
                <select name="environment" className="form-control" value={formData.environment} onChange={handleFormChange}>
                  <option value="Production">Production</option>
                  <option value="Staging">Staging</option>
                  <option value="Development">Development</option>
                </select>
              </div>
              <div className="form-group">
                <label>Owner Team *</label>
                <input type="text" name="ownerTeam" className="form-control" value={formData.ownerTeam} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>Status</label>
                <select name="status" className="form-control" value={formData.status} onChange={handleFormChange}>
                  <option value="ACTIVE">ACTIVE</option>
                  <option value="MAINTENANCE">MAINTENANCE</option>
                  <option value="OFFLINE">OFFLINE</option>
                </select>
              </div>
              <div className="form-actions">
                <button type="button" className="btn btn-secondary" onClick={handleCloseForm}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}

      <ConfirmDialog 
        isOpen={isConfirmOpen}
        title="Delete Server"
        message={`Are you sure you want to delete server ${currentServer?.hostname}?`}
        onConfirm={confirmDelete}
        onCancel={() => setIsConfirmOpen(false)}
      />
    </div>
  );
}

export default Servers;
