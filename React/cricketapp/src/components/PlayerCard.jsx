function PlayerCard({ name, runs }) {
  return (
    <div>
      <h2>{name}</h2>
      <p>Runs: {runs}</p>
    </div>
  );
}

export default PlayerCard;
