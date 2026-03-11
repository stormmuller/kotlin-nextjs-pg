import { api } from '../src/services/api';

global.fetch = jest.fn();

describe('api service', () => {
  beforeEach(() => {
    (fetch as jest.Mock).mockClear();
  });

  describe('getItems', () => {
    it('returns items on success', async () => {
      const items = [{ id: 1, name: 'Apple', price: '1.99' }];
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: true,
        json: async () => items,
      });

      const result = await api.getItems();
      expect(result).toEqual(items);
      expect(fetch).toHaveBeenCalledWith(expect.stringContaining('/items'));
    });

    it('throws on error response', async () => {
      (fetch as jest.Mock).mockResolvedValueOnce({ ok: false, status: 500 });
      await expect(api.getItems()).rejects.toThrow();
    });
  });

  describe('createItem', () => {
    it('posts item and returns created item', async () => {
      const item = { id: 1, name: 'Apple', price: '1.99' };
      (fetch as jest.Mock).mockResolvedValueOnce({
        ok: true,
        json: async () => item,
      });

      const result = await api.createItem({ name: 'Apple', price: '1.99' });
      expect(result).toEqual(item);
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining('/items'),
        expect.objectContaining({ method: 'POST' })
      );
    });
  });

  describe('deleteItem', () => {
    it('calls DELETE endpoint', async () => {
      (fetch as jest.Mock).mockResolvedValueOnce({ ok: true, status: 204 });
      await api.deleteItem(1);
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining('/items/1'),
        expect.objectContaining({ method: 'DELETE' })
      );
    });
  });
});
