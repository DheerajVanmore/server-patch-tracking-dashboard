import API from './api';

export const getPatchEvents = async (params) => {
  const response = await API.get('/patch-events', { params });
  return response.data;
};

export const getPatchEventById = async (id) => {
  const response = await API.get(`/patch-events/${id}`);
  return response.data;
};

export const createPatchEvent = async (data) => {
  const response = await API.post('/patch-events', data);
  return response.data;
};

export const updatePatchEvent = async (id, data) => {
  const response = await API.put(`/patch-events/${id}`, data);
  return response.data;
};
