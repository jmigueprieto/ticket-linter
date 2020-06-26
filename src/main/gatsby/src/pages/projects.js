import React, { useEffect, useState } from "react";
import { Grid, GridColumn } from "@atlaskit/page";

import Layout from "../components/Layout";
import Projects from "../components/Projects";
import { atlassian } from "../libraries/atlassian";

// FIXME link to enforced rules
export default () => {
  // Move this to Wrapper
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    getProjects(setProjects, setLoading);
  }, []);

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
          <Projects
            projects={projects}
            loading={loading}
            onEvaluate={async (key) => {
              setLoading(true);
              const evaluation = await evaluateProject(key);
              const project = projects.find((project) => project.key === key);
              project.violations = evaluation.violations;
              project.total = evaluation.total;
              project.lastUpdate = evaluation.timestamp;
              project.issues = evaluation.stories.filter((story) => !story.passes);
              setProjects([...projects]);
              setLoading(false);
            }}
          />
        </GridColumn>
      </Grid>
    </Layout>
  );
};

//FIXME externalize host
async function getProjects(setProjects, setLoading) {
  setLoading(true);
  try {
    const token = await atlassian.AP.context.getToken();
    console.log(`token: ${token}`);
    const response = await fetch(`https://covidio.ngrok.io/linter/api/projects?jwt=${token}`, {
      method: "GET",
      mode: "cors",
    });
    const projects = await response.json();
    console.log("==== PROJECTS ====");
    console.log(projects);
    console.log("==================");
    setProjects(projects);
  } catch (e) {}
  setLoading(false);
}

async function evaluateProject(key) {
  try {
    const token = await atlassian.AP.context.getToken();
    console.log(`token: ${token}`);
    const response = await fetch(`https://covidio.ngrok.io/linter/api/projects/${key}/evaluation?jwt=${token}`, {
      method: "GET",
      mode: "cors",
    });
    const evaluation = await response.json();
    console.log("==== EVALUATION ====");
    console.log(evaluation);
    console.log("====================");
    return evaluation;
  } catch (e) {
    console.log(`error while getting evaluations of project ${key}`);
  }
}
