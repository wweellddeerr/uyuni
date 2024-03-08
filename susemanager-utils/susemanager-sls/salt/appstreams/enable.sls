{% if pillar.get('param_appstreams') %}
appstream_enabled:
  appstream.enabled:
    - appstreams:
{%- for module_name, stream in pillar.get('param_appstreams', []) %}
      - {{ module_name }}:{{ stream }}
{%- endfor %}
{% endif %}
