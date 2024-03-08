import { useState } from "react";
import { Button } from "components/buttons";
import { ChannelModules } from "./channel-modules";
import { Dialog } from "../../components/dialog/Dialog";
import { ModulePackages } from "./module-packages";

export const AppStreamsList = ({
  channelsModules,
  toEnable,
  toDisable,
  onSubmitChanges,
  onModuleEnableDisable
}) => {
  
  const [moduleToShowPackages, setModuleToShowPackages] = useState<{ stream: string; channelId: number } | null>(null);
  const numberOfChanges = toEnable.length + toDisable.length;

  return (
    <>
      <p>{t("Use the status button for changes and then confirm using the Enable/Disable AppStreams button.")}</p>
      <div className="text-right margin-bottom-sm">
        <Button
          id="applyModuleChanges"
          icon="fa-plus"
          className="btn-success"
          disabled={numberOfChanges === 0}
          text={t("Enable/Disable AppStreams") + (numberOfChanges > 0 ? " (" + numberOfChanges + ")" : "")}
          handler={onSubmitChanges}
        />
      </div>
      <div className="panel panel-default">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Channel</th>
              <th>Modules</th>
              <th>Streams</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {channelsModules.map((channelModule) => {
              const { numberOfAppStreams, modules, modulesNames, channelId, channelLabel } = channelModule;
              return modulesNames.map((moduleName, channelIdx) => (
                <ChannelModules
                  showPackages={(stream) => setModuleToShowPackages({ stream, channelId })}
                  key={moduleName}
                  modules={modules}
                  moduleName={moduleName}
                  channelIdx={channelIdx}
                  channelId={channelId}
                  channelLabel={channelLabel}
                  numberOfAppStreams={numberOfAppStreams}
                  toEnable={toEnable}
                  toDisable={toDisable}
                  onToggle={onModuleEnableDisable}
                />
              ));
            })}
          </tbody>
        </table>
      </div>
      <Dialog
        isOpen={moduleToShowPackages !== null}
        onClose={() => setModuleToShowPackages(null)}
        title="Packages"
        className="modal-lg"
        id="modulePackagesPopUp"
        content={<ModulePackages stream={moduleToShowPackages?.stream} channelId={moduleToShowPackages?.channelId} />}
      />
    </>
  )
}
