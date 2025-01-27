from importlib.resources import Package
from typing_extensions import override

from hexdoc.plugin import (
    HookReturn,
    ModPlugin,
    ModPluginImpl,
    ModPluginWithBook,
    hookimpl,
)

import hexdoc_overevaluate

from .__gradle_version__ import FULL_VERSION, GRADLE_VERSION
from .__version__ import PY_VERSION


class OverevaluatePlugin(ModPluginImpl):
    @staticmethod
    @hookimpl
    def hexdoc_mod_plugin(branch: str) -> ModPlugin:
        return OverevaluateModPlugin(branch=branch)


class OverevaluateModPlugin(ModPluginWithBook):
    @property
    @override
    def modid(self) -> str:
        return "overevaluate"

    @property
    @override
    def full_version(self) -> str:
        return FULL_VERSION

    @property
    @override
    def mod_version(self) -> str:
        return GRADLE_VERSION

    @property
    @override
    def plugin_version(self) -> str:
        return PY_VERSION

    @override
    def resource_dirs(self) -> HookReturn[Package]:
        # lazy import because generated may not exist when this file is loaded
        # eg. when generating the contents of generated
        # so we only want to import it if we actually need it
        from ._export import generated

        return generated
    
    @override
    def jinja_template_root(self) -> tuple[Package, str]:
        return hexdoc_overevaluate, "_templates"
