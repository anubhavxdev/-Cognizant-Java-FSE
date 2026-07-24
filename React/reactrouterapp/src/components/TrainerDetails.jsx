import { useParams } from "react-router-dom";
import trainers from "../TrainersMock";

function TrainerDetails() {
  const { id } = useParams();

  const trainer = trainers.find((t) => t.id === Number(id));

  return (
    <div>
      <h2>{trainer.name}</h2>

      <p>Email : {trainer.email}</p>

      <p>Phone : {trainer.phone}</p>

      <p>Technology : {trainer.technology}</p>

      <p>Skills : {trainer.skills}</p>
    </div>
  );
}

export default TrainerDetails;
