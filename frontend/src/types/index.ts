export interface Item {
  id: number;
  name: string;
  price: string;
}

export interface CreateItemRequest {
  name: string;
  price: string;
}

export type RootStackParamList = {
  Home: undefined;
  AddItem: undefined;
};
