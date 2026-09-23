import { useState, useEffect } from 'react';
import { getPatches, createPatch, updatePatch, deletePatch } from '../services/patchService';
import SeverityBadge from '../components/SeverityBadge';
import SearchBar from '../components/SearchBar';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import ConfirmDialog from '../components/ConfirmDialog';

function Patches() {
  const [patches, setPatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [search, setSearch] = useState('');
  const [severity, setSeverity] = useState('');
  
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [isConfirmOpen, setIsConfirmOpen] = useState(false);
  const [currentPatch, setCurrentPatch] = useState(null);
  
  const [formData, setFormData] = useState({
    patchName: '', patchIdentifier: '', version: '', releaseDate: '', severity: 'LOW', description: ''
  });

  const fetchPatches = async () => {
    setLoading(true);
    try {
      const params = {};
      if (search) params.search = search;
      if (severity) params.severity = severity;
      
      const data = await getPatches(params);
      setPatches(data);
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch patches');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      fetchPatches();
    }, 300);
    return () => clearTimeout(delayDebounceFn);
  }, [search, severity]);

  const handleOpenForm = (patch = null) => {
    if (patch) {
      setCurrentPatch(patch);
      setFormData({
        patchName: patch.patchName,
        patchIdentifier: patch.patchIdentifier,
        version: patch.version,
        releaseDate: patch.releaseDate ? new Date(patch.releaseDate).toISOString().split('T')[0] : '',
        severity: patch.severity,
        description: patch.description || ''
      });
    } else {
      setCurrentPatch(null);
      setFormData({
        patchName: '', patchIdentifier: '', version: '', releaseDate: '', severity: 'LOW', description: ''
      });
    }
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
    setCurrentPatch(null);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (currentPatch) {
        await updatePatch(currentPatch.id, formData);
      } else {
        await createPatch(formData);
      }
      handleCloseForm();
      fetchPatches();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to save patch');
    }
  };

  const handleDeleteClick = (patch) => {
    setCurrentPatch(patch);
    setIsConfirmOpen(true);
  };

  const confirmDelete = async () => {
    try {
      await deletePatch(currentPatch.id);
      setIsConfirmOpen(false);
      setCurrentPatch(null);
      fetchPatches();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to delete patch');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h2 className="page-title">Patches</h2>
        <button className="btn btn-primary" onClick={() => handleOpenForm()}>Add Patch</button>
      </div>

      <div className="filters-bar">
        <div className="filter-item">
          <SearchBar value={search} onChange={setSearch} placeholder="Search by name or identifier..." />
        </div>
        <div className="filter-item">
          <select className="form-control" value={severity} onChange={(e) => setSeverity(e.target.value)}>
            <option value="">All Severities</option>
            <option value="CRITICAL">CRITICAL</option>
            <option value="HIGH">HIGH</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="LOW">LOW</option>
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
                  <th>Patch Name</th>
                  <th>Identifier</th>
                  <th>Version</th>
                  <th>Release Date</th>
                  <th>Severity</th>
                  <th>Description</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {patches.length > 0 ? (
                  patches.map(patch => (
                    <tr key={patch.id}>
                      <td>{patch.patchName}</td>
                      <td>{patch.patchIdentifier}</td>
                      <td>{patch.version}</td>
                      <td>{patch.releaseDate ? new Date(patch.releaseDate).toLocaleDateString() : 'N/A'}</td>
                      <td><SeverityBadge severity={patch.severity} /></td>
                      <td style={{ maxWidth: '200px', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis' }}>{patch.description}</td>
                      <td>
                        <div className="flex gap-2">
                          <button className="btn btn-secondary" onClick={() => handleOpenForm(patch)}>Edit</button>
                          <button className="btn btn-danger" onClick={() => handleDeleteClick(patch)}>Delete</button>
                        </div>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>No patches found.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {isFormOpen && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2 className="modal-title">{currentPatch ? 'Edit Patch' : 'Add Patch'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Patch Name *</label>
                <input type="text" name="patchName" className="form-control" value={formData.patchName} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>Patch Identifier *</label>
                <input type="text" name="patchIdentifier" className="form-control" value={formData.patchIdentifier} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>Version *</label>
                <input type="text" name="version" className="form-control" value={formData.version} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>Release Date</label>
                <input type="date" name="releaseDate" className="form-control" value={formData.releaseDate} onChange={handleFormChange} required />
              </div>
              <div className="form-group">
                <label>Severity</label>
                <select name="severity" className="form-control" value={formData.severity} onChange={handleFormChange}>
                  <option value="CRITICAL">CRITICAL</option>
                  <option value="HIGH">HIGH</option>
                  <option value="MEDIUM">MEDIUM</option>
                  <option value="LOW">LOW</option>
                </select>
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea name="description" className="form-control" value={formData.description} onChange={handleFormChange} rows="3"></textarea>
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
        title="Delete Patch"
        message={`Are you sure you want to delete patch ${currentPatch?.patchName}?`}
        onConfirm={confirmDelete}
        onCancel={() => setIsConfirmOpen(false)}
      />
    </div>
  );
}

export default Patches;
