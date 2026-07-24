import { Link } from "react-router-dom";
import trainers from "../TrainersMock";

function TrainersList() {
  return (
    <div>
      <h2>Trainers</h2>

      <ul>
        {trainers.map((trainer) => (
          <li key={trainer.id}>
            <Link to={`/trainer/${trainer.id}`}>{trainer.name}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default TrainersList;
