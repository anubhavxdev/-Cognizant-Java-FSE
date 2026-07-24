import { useState } from "react";

import ThemeContext from "./ThemeContext";

import EmployeeList from "./components/EmployeeList";

function App() {
  const [theme] = useState("dark");

  return (
    <ThemeContext.Provider value={theme}>
      <EmployeeList />
    </ThemeContext.Provider>
  );
}

export default App;
