import commands
import re

def resolve_by_consul(member_name):
  member_if = commands.getoutput(
    "/usr/local/bin/consul members | grep " + member_name + " | awk '{print $2}'")
  member_addr = re.sub(r'\:.*', '', member_if)
  return member_addr if member_addr != "" else member_name


class FilterModule(object):
  def filters(self):
    return {'consul': resolve_by_consul}
