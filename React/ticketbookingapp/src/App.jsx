import { useState } from "react";

function App() {
  const [seats, setSeats] = useState(5);

  const bookTicket = () => {
    if (seats > 0) {
      setSeats(seats - 1);
    }
  };

  return (
    <div
      style={{
        textAlign: "center",
        marginTop: "50px",
      }}
    >
      <h1>Ticket Booking App</h1>

      <h2>Available Seats : {seats}</h2>

      <>
        {seats > 0 && <button onClick={bookTicket}>Book Ticket</button>}

        {seats === 0 && <h2 style={{ color: "red" }}>House Full</h2>}
      </>
    </div>
  );
}

export default App;
