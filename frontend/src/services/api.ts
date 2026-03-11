import { Item, CreateItemRequest } from '../types';

const BASE_URL = process.env.EXPO_PUBLIC_API_URL ?? 'http://localhost:8080';

export const api = {
  getItems: async (): Promise<Item[]> => {
    const response = await fetch(`${BASE_URL}/items`);
    if (!response.ok) {
      throw new Error(`Failed to fetch items: ${response.status}`);
    }
    return response.json();
  },

  createItem: async (item: CreateItemRequest): Promise<Item> => {
    const response = await fetch(`${BASE_URL}/items`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(item),
    });
    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.error ?? `Failed to create item: ${response.status}`);
    }
    return response.json();
  },

  deleteItem: async (id: number): Promise<void> => {
    const response = await fetch(`${BASE_URL}/items/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok && response.status !== 204) {
      throw new Error(`Failed to delete item: ${response.status}`);
    }
  },
};
