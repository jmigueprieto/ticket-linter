import React from "react";
import { Grid, GridColumn } from "@atlaskit/page";

import Layout from "../components/Layout";
import Projects from "../components/Projects";

// FIXME link to enforced rules
const ProjectsPage = ({ loading, projects, onEvaluate }) => {
  return (
    <Layout>
      <Grid>
        <GridColumn medium={8}>
          <h1>Project Evaluation</h1>
          <p>
            When you run an Evaluation,{" "}
            <span style={{ fontWeight: "bold" }}>Story Linter goes through all currently open user stories</span> in a
            project and reports <span style={{ fontWeight: "bold" }}>violations to the enforced rules</span> (e.g.
            stories that are not expressed as expressed as{" "}
            <span style={{ fontStyle: "italic" }}>“persona + need + purpose”</span> or don't have an acceptance
            criteria)
          </p>
        </GridColumn>
        <GridColumn medium={4}>
          <h2>More info</h2>
          <ul>
            <li>
              <a
                href="https://www.atlassian.com/agile/project-management/user-stories"
                target="_blank"
                rel="noreferrer"
              >
                Enforced rules
              </a>
            </li>
            <li>
              <a
                href="https://www.atlassian.com/agile/project-management/user-stories"
                target="_blank"
                rel="noreferrer"
              >
                Examples
              </a>
            </li>
          </ul>
        </GridColumn>
        <GridColumn>
          <Projects projects={projects} loading={loading} onEvaluate={onEvaluate} />
        </GridColumn>
      </Grid>
    </Layout>
  );
};

ProjectsPage.defaultProps = {
  loading: true,
  projects: [],
  onEvaluate: () => {},
};

export default ProjectsPage;
