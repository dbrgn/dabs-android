# -*- coding: utf-8 -*-
from __future__ import print_function, division, absolute_import, unicode_literals

import re
import urllib
import subprocess
from datetime import date

import tablib
from bottle import route, run, response, static_file


TEMPPATH = '.'


def extract_map(infile, outfile):
    subprocess.check_call(['mudraw', '-r', '150', '-o', outfile, infile, '1'])
    return outfile


def extract_text(infile):
    # Get text from mudraw
    text = subprocess.check_output(['mudraw', '-t', infile])

    # Cleanup raw text
    match = re.search(
            r'.*?Activity \/ Remarks(?P<table1>.*?)Activities not shown on the ' +
            r'DABS Chart Side:.*?Activity \/ Remarks(?P<table2>.*?)For detailed ' +
            r'information regarding the DABS',
            text,
            re.MULTILINE | re.DOTALL)
    if not match:
        response.status = 500
        return 'Could not extract text from PDF.'
    data = '\n\n\n'.join(match.groups())
    raw_parts = re.sub(r'\n[ \t]+\n', '\n\n', data).split('\n\n\n')
    parts = filter(None, map(lambda x: x.strip(), raw_parts))

    # Write CSV
    headers = (
        'Firing-Nr\nD-/R-Area\nNOTAM-Nr',
        'Validity UTC',
        'Lower Limit\nAMSL or FL',
        'Upper Limit\nAMSL or FL',
        'Location',
        'Center Point',
        'Covering Radius',
        'Activity / Remarks',
    )
    rows = []
    for part in parts:
        row = {}
        step1 = re.split(r'([0-2][0-9][0-6][0-9] - [0-2][0-9][0-6][0-9])', part)
        row['nr'] = step1[0].strip()
        timestring = '\n'.join(step1[1:-1])
        row['validity'] = re.sub(r'\n+', '\n', timestring)
        height_re = r'(GND|[0-9]+m \/ [0-9]+ft|FL[0-9]{2,3})'
        step2 = filter(None, re.split(height_re, step1[-1].strip()))
        row['lower'] = step2[0]
        row['upper'] = step2[2]
        rows.append((
            row['nr'],
            row['validity'],
            row['lower'],
            row['upper'],
            '',
            '',
            '',
            '',
        ))
    data = tablib.Dataset(*rows, headers=headers)
    return data.csv



@route('/')
def index():
    return 'DABS webservice'


@route('/today/<contenttype>/')
def today(contenttype):
    # Fetch today's DABS from Skyguide
    datestring = date.today().strftime('%Y%m%d')
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
