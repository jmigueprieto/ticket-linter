import React, { useState } from "react";
import { Grid, GridColumn } from "@atlaskit/page";
import Layout from "../components/Layout";
import Projects from "../components/Projects";
import LinksMenu from "../components/LinksMenu";
import { renderActiveSpotlight } from "../libraries/tour";

import {
  SpotlightManager,
  SpotlightTarget,
  SpotlightTransition,
} from "@atlaskit/onboarding";
import { MockProjectsTable } from "../components/MockProjects";
import { render } from "react-dom";

const ProjectsPage = ({ loading, projects, onEvaluate }) => {
  const [active, setActive] = useState(null);

  const start = () => setActive(0);
  const next = () => setActive((active || 0) + 1);
  const prev = () => setActive((active || 0) - 1);
  const finish = () => {
    console.log("FINITO");
    setActive(null);
  };

  const actions = {
    next: next,
    prev: prev,
    finish: finish,
    start: start,
  };

  return (
    <Layout>
      <SpotlightManager>
        <Grid>
          <GridColumn medium={8}>
            <h1>Story Linter</h1>
            <p>
              A Project Scan{" "}
              <span style={{ fontWeight: "bold" }}>
                evaluates all currently open user stories
              </span>{" "}
              in the project and reports{" "}
              <span style={{ fontWeight: "bold" }}>
                violations to the rules
              </span>{" "}
              (e.g. stories that are not expressed as expressed as{" "}
              <span style={{ fontStyle: "italic" }}>
                “persona + need + purpose”
              </span>{" "}
              or don't have an acceptance criteria)
            </p>
          </GridColumn>
          <GridColumn medium={4}>
            <LinksMenu setActive={setActive} />
          </GridColumn>
          <GridColumn>
            <div style={{ marginTop: "2rem" }}>
              {active ? (
                <MockProjectsTable />
              ) : (
                <Projects
                  projects={projects}
                  loading={loading}
                  onEvaluate={onEvaluate}
                />
              )}
            </div>
          </GridColumn>
        </Grid>
        <SpotlightTransition>
          {renderActiveSpotlight(active, actions)}
        </SpotlightTransition>
      </SpotlightManager>
    </Layout>
  );
};

ProjectsPage.defaultProps = {
  loading: true,
  projects: [],
  onEvaluate: () => {},
};

export default ProjectsPage;
