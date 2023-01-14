import { dayStrings } from '@/assets'

const parseTime = (minutes) => {
    let hours = Math.floor(minutes / 60)
    minutes = Math.floor(minutes % 60)

    if (hours < 10) hours = '0' + hours
    if (minutes < 10) minutes = '0' + minutes

    return hours + ':' + minutes
}

const timeString = (group) =>
    `${dayStrings[group.day]} ${parseTime(group.timeStart)} - ${parseTime(
        group.timeEnd
    )}`

export { parseTime, timeString }
