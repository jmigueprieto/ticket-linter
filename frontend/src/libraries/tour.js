import React from "react";
import { Spotlight } from "@atlaskit/onboarding";

export const renderActiveSpotlight = (active, actions) => {
  const variants = [
    <Spotlight
      actions={[
        { onClick: actions.next, text: "Start Tour" },
        { onClick: actions.finish, text: "Dismiss", appearance: "subtle" },
      ]}
      dialogPlacement="left middle"
      heading="Scan Project"
      target="start"
      key="start"
      targetBgColor="white"
    >
      <p>
        In the following steps we will show you how Story Linter works and what
        are the current capabilities.
      </p>
      <p> Please press "Start Tour" to get started.</p>
    </Spotlight>,
    <Spotlight
      actions={[
        { onClick: actions.next, text: "next" },
        { onClick: actions.finish, text: "Dismiss", appearance: "subtle" },
      ]}
      dialogPlacement="left top"
      heading="Scan Project"
      target="scan-project"
      key="scan-project"
      targetBgColor="white"
    >
      By pressing "Scan Project button Story Linter will scan through all of
      your projects' OPEN User Stories to check for erros defined in "Enforced
      Rules".
    </Spotlight>,
    <Spotlight
      actions={[
        { onClick: actions.next, text: "next" },
        { onClick: actions.finish, text: "Dismiss", appearance: "subtle" },
      ]}
      dialogPlacement="bottom center"
      heading="Scan Results"
      target="scan-results"
      key="scan-results"
      targetBgColor="white"
    >
      Once Story Linter finishes it shows you:
      <ul>
        <li>Date of the last scan (Last Evaluation)</li>
        <li>The number of stories scanned (Total)</li>
        <li>
          The number of violations (Didn't meet at least one of the rules)
        </li>
      </ul>
    </Spotlight>,
    <Spotlight
      actions={[
        { onClick: actions.next, text: "next" },
        { onClick: actions.finish, text: "Dismiss", appearance: "subtle" },
      ]}
      heading="View Results"
      target="view-results"
      key="view-results"
      targetBgColor="white"
    >
      <p>
        If there's any violations, this View Results button will appear so you
        can yo to the details of what stories violated the enforced rules.
      </p>
    </Spotlight>,
  ];
  if (active === null) return null;
  return variants[active];
};
