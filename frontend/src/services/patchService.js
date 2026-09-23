import API from './api';

export const getPatches = async (params) => {
  const response = await API.get('/patches', { params });
  return response.data;
};

export const getPatchById = async (id) => {
  const response = await API.get(`/patches/${id}`);
  return response.data;
};

export const createPatch = async (data) => {
  const response = await API.post('/patches', data);
  return response.data;
};

export const updatePatch = async (id, data) => {
  const response = await API.put(`/patches/${id}`, data);
  return response.data;
};

export const deletePatch = async (id) => {
  const response = await API.delete(`/patches/${id}`);
  return response.data;
};
