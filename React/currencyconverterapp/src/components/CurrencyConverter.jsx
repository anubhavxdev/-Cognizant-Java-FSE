import { useState } from "react";

function CurrencyConvertor() {
  const [rupees, setRupees] = useState("");
  const [euro, setEuro] = useState("");

  const handleSubmit = () => {
    const exchangeRate = 90;

    const convertedValue = (Number(rupees) / exchangeRate).toFixed(2);

    setEuro(convertedValue);
  };

  return (
    <div
      style={{
        textAlign: "center",
        marginTop: "40px",
      }}
    >
      <h1>Currency Convertor</h1>

      <input
        type="number"
        placeholder="Enter Amount in INR"
        value={rupees}
        onChange={(e) => setRupees(e.target.value)}
      />

      <br />
      <br />

      <button onClick={handleSubmit}>Convert</button>

      <br />
      <br />

      {euro !== "" && <h2>€ {euro}</h2>}
    </div>
  );
}

export default CurrencyConvertor;
