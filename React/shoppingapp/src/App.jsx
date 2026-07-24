import ProductList from "./components/ProductList";

function App() {
  const products = [
    {
      id: 1,
      name: "Laptop",
      price: 55000,
    },
    {
      id: 2,
      name: "Smartphone",
      price: 30000,
    },
    {
      id: 3,
      name: "Headphones",
      price: 2500,
    },
    {
      id: 4,
      name: "Keyboard",
      price: 1800,
    },
  ];

  return <ProductList products={products} />;
}

export default App;
