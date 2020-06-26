/**
 * Implement Gatsby's SSR (Server Side Rendering) APIs in this file.
 *
 * See: https://www.gatsbyjs.org/docs/ssr-apis/
 */
var React = require("react");

exports.onRenderBody = ({ setHeadComponents }) => {};

exports.onPreRenderHTML = ({ getHeadComponents, replaceHeadComponents }) => {
  const headComponents = getHeadComponents();
  headComponents.push(<script key="atlassian" src="https://connect-cdn.atl-paas.net/all-debug.js" />);

  replaceHeadComponents(headComponents);
};
