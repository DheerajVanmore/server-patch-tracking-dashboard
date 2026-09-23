import API from './api';

export const getSummary = async () => {
  const response = await API.get('/dashboard/summary');
  return response.data;
};
