{% if pillar.get('param_appstreams') %}
appstream_disabled:
  appstream.disabled:
    - appstreams:
{%- for module_name, stream in pillar.get('param_appstreams', []) %}
      - {{ module_name }}
{%- endfor %}
{% endif %}
