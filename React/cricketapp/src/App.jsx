import PlayerCard from "./components/PlayerCard";

function App() {
  const players = [
    { id: 1, name: "Virat Kohli", runs: 13848 },
    { id: 2, name: "Rohit Sharma", runs: 11168 },
    { id: 3, name: "Shubman Gill", runs: 2500 },
  ];

  return (
    <div>
      <h1>Indian Cricket Team</h1>

      {players.map((player) => (
        <PlayerCard key={player.id} name={player.name} runs={player.runs} />
      ))}
    </div>
  );
}

export default App;
