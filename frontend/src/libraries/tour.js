import React from "react";
import { Spotlight } from "@atlaskit/onboarding";
import { Modal } from "@atlaskit/onboarding";
import linterResultsImg from "../images/sample-story.png";

export const renderActiveSpotlight = (active, actions) => {
  const variants = [
    <Modal
      actions={[{ onClick: actions.next, text: "Start Tour" }]}
      heading="Welcome to Story Linter"
      key="welcome"
    >
      <p>
        In the following steps we will show you how Story Linter works and what
        are the current capabilities.
      </p>
      <p> Please press "Start Tour" to get started.</p>
    </Modal>,
    <Spotlight
      actions={[{ onClick: actions.next, text: "next" }]}
      dialogPlacement="bottom left"
      heading="Scan Project"
      target="step-1"
      key="step-1"
      targetBgColor="white"
    >
      By pressing "Scan Project button Story Linter will scan through all of
      your projects' OPEN User Stories to check for erros defined in "Enforced
      Rules".
    </Spotlight>,
    <Spotlight
      actions={[{ onClick: actions.next, text: "next" }]}
      dialogPlacement="bottom left"
      heading="Scan Results"
      target="step-2"
      key="step-2"
      targetBgColor="white"
    >
      Once Story Linter finishes it shows you: <br />
      - Date of the last scan (Last Evaluation)
      <br />
      - The number of stories scanned (Total)
      <br />- The number of violations (Didn't meet at least one of the rules)
    </Spotlight>,
    <Spotlight
      actions={[{ onClick: actions.next, text: "next" }]}
      dialogPlacement="bottom left"
      heading="View Results"
      target="step-3"
      key="step-3"
      targetBgColor="white"
    >
      If there's any violations, this View Results button will appear so you can
      yo to the details of what stories violated the enforced rules.
    </Spotlight>,
  ];
  debugger;
  if (active === 4) {
    return (
      <Modal
        actions={[{ onClick: actions.finish, text: "End Tour" }]}
        heading="Linter Evaluation Results"
        target="step-4"
        key="step-4"
      >
        <p>
          <img
            width="100%"
            src={linterResultsImg}
            alt="Story Evaluation Results"
          />
        </p>
      </Modal>
    );
  }
  if (active === null) return null;

  return variants[active];
};
