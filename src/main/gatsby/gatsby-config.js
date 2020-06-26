module.exports = {
  siteMetadata: {
    title: `User Story Linter`,
    description: `Jira addon that checks your user stories for formatting or stylistic errors`,
    author: `@mprieto`,
  },
  plugins: [
    `babel-plugin-styled-components`,
    `gatsby-plugin-styled-components`,
    // {
    //   resolve: "gatsby-plugin-load-script",
    //   options: {
    //     src: "https://connect-cdn.atl-paas.net/all.js",
    //   },
    // },
  ],
};
