import "../styles/mystyle.css";

function CalculateScore(props) {
  const average = ((props.total / props.goal) * 100).toFixed(2);

  return (
    <div className="card">
      <h1>Student Details</h1>

      <p>
        <b>Name:</b> {props.name}
      </p>

      <p>
        <b>School:</b> {props.school}
      </p>

      <p>
        <b>Total Marks:</b> {props.total}
      </p>

      <p>
        <b>Goal:</b> {props.goal}
      </p>

      <h2>Average Score: {average}%</h2>
    </div>
  );
}

export default CalculateScore;
