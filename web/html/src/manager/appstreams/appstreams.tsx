import { useState } from "react";

import { ChannelModule, AppStreamModule } from "./appstreams.type";

import { AppStreamsList } from "./list-appstreams";
import { AppStreamsChangesConfirm } from "./changes-confirm-appstreams";

type Props = {
  channelsModules: Array<ChannelModule>;
};

const AppStreams = (props: Props) => {
  const [toEnable, setToEnable] = useState<Array<string>>([]);
  const [toDisable, setToDisable] = useState<Array<string>>([]);
  const [showConfirm, setShowConfirm] = useState<boolean>(false);
  const [confirmed, setConfirmed] = useState<boolean>(false);
  const [scheduledMsg, setScheduledMsg] = useState<any>(null);

  const handleEnableDisable = (appStream: AppStreamModule) => {
    const stream = `${appStream.name}:${appStream.stream}`;
    if (appStream.enabled) {
      setToDisable((prevState) =>
        prevState.includes(stream) ? prevState.filter((it) => it !== stream) : prevState.concat(stream)
      );
    } else {
      setToEnable((prevState) =>
        prevState.includes(stream) ? prevState.filter((it) => it !== stream) : prevState.concat(stream)
      );
    }
  };

  const handleSubmitChanges = () => {
    setShowConfirm(true);
  }

  const handleConfirmChanges = (msg) => {
    setShowConfirm(false);
    setConfirmed(true);
    setScheduledMsg(msg);
  }

  const showContent = () => {
    if (confirmed) {
      return scheduledMsg;
    }
    else if (showConfirm) {
      return (
        <AppStreamsChangesConfirm
          toEnable={toEnable}
          toDisable={toDisable}
          onCancelClick={() => setShowConfirm(false)}
          onConfirm={handleConfirmChanges}
        />
      );
    }
    else {
      return (
        <AppStreamsList
            toEnable={toEnable}
            toDisable={toDisable}
            onModuleEnableDisable={handleEnableDisable}
            onSubmitChanges={handleSubmitChanges}
            channelsModules={props.channelsModules} />
      );
    }
  }

  return (
    <>
      <h2>
        <i className={"fa spacewalk-icon-salt-add"} />
        {t("AppStreams")}
        &nbsp;
      </h2>
      
      { showContent() }
    </>
  );
};

export default AppStreams;
