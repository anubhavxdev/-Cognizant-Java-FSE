import { useState } from "react";

function App() {
  const [name, setName] = useState("");
  const [isSubmitted, setIsSubmitted] = useState(false);

  const handleClick = () => {
    alert("Button Clicked!");
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    setIsSubmitted(true);
    alert(`Welcome ${name}`);
  };

  return (
    <div style={{ textAlign: "center", marginTop: "40px" }}>
      <h1>React Event Examples</h1>

      <button onClick={handleClick}>Click Me</button>

      <br />
      <br />

      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Enter your name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <br />
        <br />

        <button type="submit">Submit</button>

        {isSubmitted && <button onClick={() => setName("")}>Clear</button>}
      </form>
    </div>
  );
}

export default App;
