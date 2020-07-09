import React from "react";
import { useState, useEffect } from "react";
import { atlassian } from "../libraries/atlassian";
import { API_BASE_URL } from "../constants";

//TODO move these API request to a Api Client class
export default ({ children }) => {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(false);
  useEffect(() => {
    console.log(`Loading projects from ${API_BASE_URL}`);
    getProjects(setProjects, setLoading);
  }, []);

  async function getProjects(setProjects, setLoading) {
    setLoading(true);
    try {
      const token = await atlassian.AP.context.getToken();
      const response = await fetch(`${API_BASE_URL}/linter/api/projects?jwt=${token}`, {
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

  async function onEvaluate(key) {
    setLoading(true);
    const evaluation = await evaluateProject(key);
    const project = projects.find((project) => project.key === key);
    project.violations = evaluation.violations;
    project.total = evaluation.total;
    project.lastUpdate = evaluation.timestamp;
    project.issues = evaluation.stories;
    setProjects([...projects]);
    setLoading(false);
  }

  async function evaluateProject(key) {
    try {
      const token = await atlassian.AP.context.getToken();
      const response = await fetch(`${API_BASE_URL}/linter/api/projects/${key}/evaluation?jwt=${token}`, {
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
  const propsToTransfer = { projects, onEvaluate, loading };
  return React.cloneElement(children, Object.assign({}, children.props, propsToTransfer));
};
