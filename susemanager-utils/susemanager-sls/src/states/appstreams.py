def change(name, appstreams_to_enable, appstreams_to_disable):
    '''
    Change the state of AppStreams.

    name
        The name of the state

    appstreams_to_enable
        List of appstreams to enable

    appstreams_to_disable
        List of appstreams to disable in the format module_name:stream
    '''
    ret = {"name": name, "result": True, "changes": { "ret": {} }, "comment": ""}

    if appstreams_to_disable:
        disabled_success, disabled_comment, disabled_changes = _do_operation(name, appstreams_to_disable, "appstreams.disable")
        if not disabled_success:
            ret["result"] = False
            ret["comment"] = disabled_comment
            return ret

        ret["changes"]["ret"]["disabled"] = disabled_changes["disabled"]

    if appstreams_to_enable:
        enabled_success, enabled_comment, enabled_changes = _do_operation(name, appstreams_to_enable, "appstreams.enable")
        if not enabled_success:
            # Re-enable previously disabled modules if enabling fails
            if appstreams_to_disable:
                _do_operation(name, appstreams_to_disable, "appstreams.enable")
            ret["result"] = False
            ret["comment"] = enabled_comment
            return ret

        ret["changes"]["ret"]["enabled"] = enabled_changes["enabled"]

    ret["changes"]["ret"]["currently_enabled"] = __salt__["appstreams.get_enabled_modules"]()
    ret["comment"] = "Successfully changed AppStreams state."

    return ret

def _do_operation(name, appstreams, operation):
    ret = {"name": name, "result": False, "changes": {}, "comment": ""}

    success, comment, changes =  __salt__[operation](appstreams)
    ret["result"] = success
    ret["comment"] = comment
    ret["changes"] = changes

    return success, comment, changes
