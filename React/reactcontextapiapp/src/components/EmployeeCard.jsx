import { useContext } from "react";

import ThemeContext from "../ThemeContext";

function EmployeeCard() {
  const theme = useContext(ThemeContext);

  return (
    <div>
      <h2>John Doe</h2>

      <button className={theme}>View Profile</button>
    </div>
  );
}

export default EmployeeCard;
