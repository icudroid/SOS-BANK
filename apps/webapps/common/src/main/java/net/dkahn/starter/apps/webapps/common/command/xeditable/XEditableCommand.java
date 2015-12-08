package net.dkahn.starter.apps.webapps.common.command.xeditable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XEditableCommand {
    private String name;
    private Integer pk;
    private Object value;
}
