const server = 'http://localhost:8000/'

const fetchApi = async (endpoint, args) => {
    const res = await fetch(server + endpoint, {
        method: 'post',
        headers: {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        },

        body: JSON.stringify({
            ...args,
            // add authorization data
        }),
    })

    const data = await res.json()

    if (data.error) {
        if (data.error.code === 401) {
            // session expired, logout and redirect to login page
        }
        throw data.error
    }

    return data
}

const printSuccess = (message) => {
    console.log('\x1b[92m%s\x1b[0m', message)
}

const printFailure = (message) => {
    console.log('\x1b[41m\x1b[97m%s\x1b[0m', message)
}

const printDivider = () => {
    console.log(
        '\x1b[90m%s\x1b[0m',
        '============================================================'
    )
}

let successfulTests = 0
let failedTests = 0
let testNumber = 0

const fetchTest = async (endpoint, args, expected, testName = '') => {
    printDivider()
    const result = await fetchApi(endpoint, args)

    if (JSON.stringify(result) === JSON.stringify(expected)) {
        if (testName) {
            printSuccess(`Test #${testNumber} '${testName}' passed!`)
        } else {
            printSuccess(`Test #${testNumber} passed!`)
        }
        successfulTests++
    } else {
        if (testName) {
            printFailure(`Test #${testNumber} '${testName}' FAILED!`)
        } else {
            printFailure(`Test #${testNumber} FAILED!`)
        }

        console.log('Got:', result)
        console.log('Expected:', expected)
        failedTests++
    }
    testNumber++
}

const main = async () => {
    try {
        // tutaj definiujemy wszystkie testy
        await fetchTest(
            'api',
            { elo: 'test' },
            { response: 'test' },
            'example test'
        )
        await fetchTest(
            'api',
            { elo: 'test' },
            { error: { message: 'sql error' } },
            'example failing test'
        )
        //

        printDivider()
        console.log('Tests passed:', successfulTests, '/', testNumber)
        console.log('Tests failed:', failedTests, '/', testNumber)

        if (failedTests === 0) {
            printSuccess('ALL TESTS PASSED!!!')
        } else {
            printFailure('SOME TESTS FAILED :(')
        }
    } catch (error) {
        printDivider()
        printFailure('An error occured, testing aborted :(')
        console.log(error)
    }
}

main()
