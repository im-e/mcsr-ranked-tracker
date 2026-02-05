import axios from 'axios';

const API_BASE_URL = '/api/mcsr';

export const userService = {
  async getUserByIdentifier(identifier) {
    const response = await axios.get(`${API_BASE_URL}/users/${identifier}`);
    return response.data;
  }
};
