import React from "react";
import { Grid, GridColumn } from "@atlaskit/page";

import Layout from "../components/Layout";
import Issues from "../components/Issues";
import { Link } from "gatsby";

const IssuesPage = ({ location }) => {
  //https://stackoverflow.com/questions/54402626/gatsby-works-fine-during-development-but-throws-error-during-build
  const { project } = location.state || { project: { name: "", issues: [] } };
  return (
    <Layout>
      <Grid>
        <GridColumn medium={12}>
          <div style={{ color: "#6b778c", fontWeight: "500", marginBottom: "1rem" }}>
            <Link style={{ color: "#6b778c" }} to="/projects">
              Linter Evaluation
            </Link>{" "}
            / {project.name}
          </div>
        </GridColumn>
        <GridColumn medium={12}>
          <h2>Results</h2>
          <Issues issues={project.issues} loading={false} />
        </GridColumn>
      </Grid>
    </Layout>
  );
};

export default IssuesPage;
