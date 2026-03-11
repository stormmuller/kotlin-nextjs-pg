import React from 'react';
import { render } from '@testing-library/react-native';
import App from '../App';

// Mock navigation
jest.mock('@react-navigation/native', () => {
  const actualNav = jest.requireActual('@react-navigation/native');
  return {
    ...actualNav,
    NavigationContainer: ({ children }: { children: React.ReactNode }) => children,
  };
});

jest.mock('@react-navigation/stack', () => ({
  createStackNavigator: () => ({
    Navigator: ({ children }: { children: React.ReactNode }) => children,
    Screen: ({ component: Component }: { component: React.ComponentType }) => <Component />,
  }),
}));

jest.mock('../src/screens/HomeScreen', () => {
  const { View, Text } = require('react-native');
  return () => (
    <View>
      <Text>Home Screen</Text>
    </View>
  );
});

jest.mock('../src/screens/AddItemScreen', () => {
  const { View, Text } = require('react-native');
  return () => (
    <View>
      <Text>Add Item Screen</Text>
    </View>
  );
});

describe('App', () => {
  it('renders without crashing', () => {
    const { getByText } = render(<App />);
    expect(getByText('Home Screen')).toBeTruthy();
  });
});
