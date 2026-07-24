import { useState } from "react";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [city, setCity] = useState("");

  const handleSubmit = (event) => {
    event.preventDefault();

    alert(
      `Registration Successful

Name : ${name}

Email : ${email}

City : ${city}`,
    );
  };

  return (
    <div
      style={{
        width: "400px",
        margin: "40px auto",
      }}
    >
      <h1>Registration Form</h1>

      <form onSubmit={handleSubmit}>
        <label>Name</label>

        <br />

        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <br />
        <br />

        <label>Email</label>

        <br />

        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <br />
        <br />

        <label>City</label>

        <br />

        <select value={city} onChange={(e) => setCity(e.target.value)}>
          <option value="">Select</option>

          <option value="Delhi">Delhi</option>

          <option value="Agra">Agra</option>

          <option value="Lucknow">Lucknow</option>
        </select>

        <br />
        <br />

        <button type="submit">Register</button>
      </form>
    </div>
  );
}

export default Register;
