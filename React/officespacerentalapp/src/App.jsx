import "./App.css";

function App() {
  const offices = [
    {
      id: 1,
      name: "Tech Park",
      rent: 45000,
      address: "Noida",
    },
    {
      id: 2,
      name: "Business Hub",
      rent: 70000,
      address: "Gurgaon",
    },
    {
      id: 3,
      name: "Corporate Tower",
      rent: 58000,
      address: "Bengaluru",
    },
  ];

  return (
    <div className="container">
      <h1>Office Space Rental</h1>

      {offices.map((office) => (
        <div className="card" key={office.id}>
          <h2>{office.name}</h2>

          <p>
            <b>Address:</b> {office.address}
          </p>

          <p
            style={{
              color: office.rent > 60000 ? "red" : "green",
            }}
          >
            <b>Rent:</b> ₹{office.rent}
          </p>
        </div>
      ))}
    </div>
  );
}

export default App;
