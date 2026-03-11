import React from 'react';
import { render, fireEvent } from '@testing-library/react-native';
import ItemCard from '../src/components/ItemCard';

const mockItem = { id: 1, name: 'Apple', price: '1.99' };

describe('ItemCard', () => {
  it('renders item name and price', () => {
    const { getByText } = render(
      <ItemCard item={mockItem} onDelete={jest.fn()} />
    );
    expect(getByText('Apple')).toBeTruthy();
    expect(getByText('$1.99')).toBeTruthy();
  });

  it('calls onDelete with correct id when Remove button pressed', () => {
    const onDelete = jest.fn();
    const { getByText } = render(
      <ItemCard item={mockItem} onDelete={onDelete} />
    );
    fireEvent.press(getByText('Remove'));
    expect(onDelete).toHaveBeenCalledWith(1);
  });
});
