.PHONY: run
run : mcp_gui/mcp_gui.pde
	processing-java --sketch=mcp_gui  --run --force --output=bin
