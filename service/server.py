# -*- coding: utf-8 -*-
from __future__ import print_function, division, absolute_import, unicode_literals

import urllib
import subprocess
from datetime import date

from bottle import route, run, response, template, static_file


TEMPPATH = '.'


def extract_map(infile, outfile):
    subprocess.check_call(['mudraw', '-r', '150', '-o', outfile, infile, '1'])
    return outfile


def extract_text(infile):
    return subprocess.check_output(['mudraw', '-t', infile])


@route('/')
def index():
    return 'DABS webservice'


@route('/today/<contenttype>/')
def today(contenttype):
    # Fetch today's DABS from Skyguide
    datestring =  date.today().strftime('%Y%m%d')
    filename = 'DABS_{0}.pdf'.format(datestring)
    url = 'http://www.skyguide.ch/fileadmin/dabs-today/' + filename
    urllib.urlretrieve(url.format(datestring), filename)

    # Render image of first page
    if contenttype == 'map':
        map_filename = extract_map(filename, 'map_{0}.png'.format(datestring))
        return static_file(map_filename, root=TEMPPATH)
    elif contenttype == 'text':
        return extract_text(filename)
    else:
        response.status = 400
        return 'Invalid content type: "{0}"'.format(contenttype)


if __name__ == '__main__':
    run(host='0.0.0.0', port=8000)
