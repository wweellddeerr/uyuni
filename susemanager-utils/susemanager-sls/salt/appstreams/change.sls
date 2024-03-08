change_appstreams:
  appstreams.change:
    - appstreams_to_disable:
{%- for module_name in pillar.get('param_appstreams_disable', []) %}
      - {{ module_name }}
{%- endfor %}
    - appstreams_to_enable:
{%- for module_name, stream in pillar.get('param_appstreams_enable', []) %}
      - {{ module_name }}:{{ stream }}
{%- endfor %}
