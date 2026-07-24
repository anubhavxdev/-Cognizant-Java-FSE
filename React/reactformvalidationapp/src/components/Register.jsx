import { useState } from "react";

function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [errors, setErrors] = useState({});

  const handleSubmit = (event) => {
    event.preventDefault();

    let validationErrors = {};

    if (name.length < 5) {
      validationErrors.name = "Name must contain at least 5 characters.";
    }

    if (!email.includes("@") || !email.includes(".")) {
      validationErrors.email = "Enter a valid email.";
    }

    if (password.length < 8) {
      validationErrors.password =
        "Password must contain at least 8 characters.";
    }

    setErrors(validationErrors);

    if (Object.keys(validationErrors).length === 0) {
      alert("Registration Successful");

      setName("");
      setEmail("");
      setPassword("");
    }
  };

  return (
    <div
      style={{
        width: "400px",
        margin: "40px auto",
      }}
    >
      <h1>Register</h1>

      <form onSubmit={handleSubmit}>
        <label>Name</label>

        <br />

        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <br />

        {errors.name && <span style={{ color: "red" }}>{errors.name}</span>}

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

        {errors.email && <span style={{ color: "red" }}>{errors.email}</span>}

        <br />
        <br />

        <label>Password</label>

        <br />

        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <br />

        {errors.password && (
          <span style={{ color: "red" }}>{errors.password}</span>
        )}

        <br />
        <br />

        <button type="submit">Register</button>
      </form>
    </div>
  );
}

export default Register;
