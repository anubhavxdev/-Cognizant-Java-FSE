import { BrowserRouter, Routes, Route, Link } from "react-router-dom";

import Home from "./components/Home";
import TrainersList from "./components/TrainersList";
import TrainerDetails from "./components/TrainerDetails";

function App() {
  return (
    <BrowserRouter>
      <nav>
        <Link to="/">Home</Link>

        {" | "}

        <Link to="/trainers">Trainers</Link>
      </nav>

      <Routes>
        <Route path="/" element={<Home />} />

        <Route path="/trainers" element={<TrainersList />} />

        <Route path="/trainer/:id" element={<TrainerDetails />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
