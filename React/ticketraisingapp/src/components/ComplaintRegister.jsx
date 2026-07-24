import { useState } from "react";

function ComplaintRegister() {
  const [employeeName, setEmployeeName] = useState("");
  const [complaint, setComplaint] = useState("");

  const handleSubmit = (event) => {
    event.preventDefault();

    const referenceNumber = Math.floor(Math.random() * 100000);

    alert(
      `Complaint Registered Successfully!

        Reference Number : ${referenceNumber}`,
    );

    setEmployeeName("");
    setComplaint("");
  };

  return (
    <div
      style={{
        width: "400px",
        margin: "40px auto",
      }}
    >
      <h1>Complaint Register</h1>

      <form onSubmit={handleSubmit}>
        <label>Employee Name</label>

        <br />

        <input
          type="text"
          value={employeeName}
          onChange={(e) => setEmployeeName(e.target.value)}
        />

        <br />
        <br />

        <label>Complaint</label>

        <br />

        <textarea
          rows="5"
          cols="40"
          value={complaint}
          onChange={(e) => setComplaint(e.target.value)}
        />

        <br />
        <br />

        <button type="submit">Submit Complaint</button>
      </form>
    </div>
  );
}

export default ComplaintRegister;
