import { useState, useEffect } from 'react';
import { getPatchEvents, createPatchEvent, updatePatchEvent } from '../services/patchEventService';
import { getServers } from '../services/serverService';
import { getPatches } from '../services/patchService';
import StatusBadge from '../components/StatusBadge';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';

function PatchEvents() {
  const [events, setEvents] = useState([]);
  const [servers, setServersList] = useState([]);
  const [patches, setPatchesList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [statusFilter, setStatusFilter] = useState('');
  const [serverFilter, setServerFilter] = useState('');
  
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentEvent, setCurrentEvent] = useState(null);
  
  const [formData, setFormData] = useState({
    serverId: '', patchId: '', status: 'PENDING', eventDate: new Date().toISOString().slice(0, 16), failureReason: '', remarks: ''
  });

  const fetchData = async () => {
    setLoading(true);
    try {
      const params = {};
      if (statusFilter) params.status = statusFilter;
      if (serverFilter) params.serverId = serverFilter;
      
      const [eventsData, serversData, patchesData] = await Promise.all([
        getPatchEvents(params),
        getServers({}),
        getPatches({})
      ]);
      
      setEvents(eventsData);
      
      if (servers.length === 0) setServersList(serversData);
      if (patches.length === 0) setPatchesList(patchesData);
      
      setError(null);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to fetch data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [statusFilter, serverFilter]);

  const handleOpenForm = (event = null) => {
    if (event) {
      setCurrentEvent(event);
      setFormData({
        serverId: event.server?.id || '',
        patchId: event.patch?.id || '',
        status: event.status,
        eventDate: event.eventDate ? new Date(event.eventDate).toISOString().slice(0, 16) : new Date().toISOString().slice(0, 16),
        failureReason: event.failureReason || '',
        remarks: event.remarks || ''
      });
    } else {
      setCurrentEvent(null);
      setFormData({
        serverId: servers.length > 0 ? servers[0].id : '',
        patchId: patches.length > 0 ? patches[0].id : '',
        status: 'PENDING',
        eventDate: new Date().toISOString().slice(0, 16),
        failureReason: '',
        remarks: ''
      });
    }
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
    setCurrentEvent(null);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // API expects nested objects for server and patch
      const payload = {
        serverId: parseInt(formData.serverId),
        patchId: parseInt(formData.patchId),
        status: formData.status,
        eventDate: new Date(formData.eventDate).toISOString(),
        failureReason: formData.status === 'FAILED' ? formData.failureReason : null,
        remarks: formData.remarks
      };

      if (currentEvent) {
        await updatePatchEvent(currentEvent.id, payload);
      } else {
        await createPatchEvent(payload);
      }
      handleCloseForm();
      fetchData();
    } catch (err) {
      alert(err.response?.data?.message || 'Failed to save patch event');
    }
  };

  return (
    <div>
      <div className="page-header">
        <h2 className="page-title">Patch Events</h2>
        <button className="btn btn-primary" onClick={() => handleOpenForm()}>Add Patch Event</button>
      </div>

      <div className="filters-bar">
        <div className="filter-item">
          <select className="form-control" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="">All Statuses</option>
            <option value="PENDING">PENDING</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="PATCHED">PATCHED</option>
            <option value="FAILED">FAILED</option>
            <option value="EXEMPTED">EXEMPTED</option>
          </select>
        </div>
        <div className="filter-item">
          <select className="form-control" value={serverFilter} onChange={(e) => setServerFilter(e.target.value)}>
            <option value="">All Servers</option>
            {servers.map(s => <option key={s.id} value={s.id}>{s.hostname}</option>)}
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
                  <th>Server</th>
                  <th>Patch</th>
                  <th>Status</th>
                  <th>Event Date</th>
                  <th>Failure Reason</th>
                  <th>Remarks</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {events.length > 0 ? (
                  events.map(event => (
                    <tr key={event.id}>
                      <td>{event.server?.hostname}</td>
                      <td>{event.patch?.patchName}</td>
                      <td><StatusBadge status={event.status} /></td>
                      <td>{new Date(event.eventDate).toLocaleString()}</td>
                      <td>{event.failureReason || '-'}</td>
                      <td>{event.remarks || '-'}</td>
                      <td>
                        <button className="btn btn-secondary" onClick={() => handleOpenForm(event)}>Edit</button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="7" style={{ textAlign: 'center', padding: '20px' }}>No patch events found.</td>
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
            <h2 className="modal-title">{currentEvent ? 'Edit Patch Event' : 'Add Patch Event'}</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Server *</label>
                <select name="serverId" className="form-control" value={formData.serverId} onChange={handleFormChange} required disabled={!!currentEvent}>
                  <option value="" disabled>Select Server</option>
                  {servers.map(s => <option key={s.id} value={s.id}>{s.hostname}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label>Patch *</label>
                <select name="patchId" className="form-control" value={formData.patchId} onChange={handleFormChange} required disabled={!!currentEvent}>
                  <option value="" disabled>Select Patch</option>
                  {patches.map(p => <option key={p.id} value={p.id}>{p.patchName}</option>)}
                </select>
              </div>
              <div className="form-group">
                <label>Status</label>
                <select name="status" className="form-control" value={formData.status} onChange={handleFormChange}>
                  <option value="PENDING">PENDING</option>
                  <option value="IN_PROGRESS">IN_PROGRESS</option>
                  <option value="PATCHED">PATCHED</option>
                  <option value="FAILED">FAILED</option>
                  <option value="EXEMPTED">EXEMPTED</option>
                </select>
              </div>
              <div className="form-group">
                <label>Event Date *</label>
                <input type="datetime-local" name="eventDate" className="form-control" value={formData.eventDate} onChange={handleFormChange} required />
              </div>
              
              {formData.status === 'FAILED' && (
                <div className="form-group">
                  <label>Failure Reason *</label>
                  <input type="text" name="failureReason" className="form-control" value={formData.failureReason} onChange={handleFormChange} required />
                </div>
              )}
              
              <div className="form-group">
                <label>Remarks</label>
                <input type="text" name="remarks" className="form-control" value={formData.remarks} onChange={handleFormChange} />
              </div>
              
              <div className="form-actions">
                <button type="button" className="btn btn-secondary" onClick={handleCloseForm}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default PatchEvents;
