import API from './api';

export const getServers = async (params) => {
  const response = await API.get('/servers', { params });
  return response.data;
};

export const getServerById = async (id) => {
  const response = await API.get(`/servers/${id}`);
  return response.data;
};

export const createServer = async (data) => {
  const response = await API.post('/servers', data);
  return response.data;
};

export const updateServer = async (id, data) => {
  const response = await API.put(`/servers/${id}`, data);
  return response.data;
};

export const deleteServer = async (id) => {
  const response = await API.delete(`/servers/${id}`);
  return response.data;
};
