import React from "react";

export default ({ children }) => {
  console.log("Loaded Wrapper");
  return <>{children}</>;
};
