import wd from 'wd';
import chai from 'chai';
import {
  androidCaps, serverConfig, androidApiDemos, SAUCE_TESTING, SAUCE_USERNAME,
  SAUCE_ACCESS_KEY
} from '../helpers/config';

const {assert} = chai;

describe('Basic Android selectors', function () {
  let driver;
  let allPassed = true;

  before(async function () {
    // Connect to Appium server
    driver = SAUCE_TESTING
      ? await wd.promiseChainRemote(serverConfig)
      : await wd.promiseChainRemote(serverConfig, SAUCE_USERNAME, SAUCE_ACCESS_KEY);

    // add the name to the desired capabilities
    const sauceCaps = SAUCE_TESTING
      ? {
        name: 'Android Selectors Tests',
      }
      : {};

    // merge all the capabilities
    const caps = {
      ...androidCaps,
      ...sauceCaps,
      app: androidApiDemos,
    };

    // Start the session, merging all the caps
    await driver.init(caps);
  });

  afterEach(function () {
    // keep track of whether all the tests have passed, since mocha does not do this
    allPassed = allPassed && (this.currentTest.state === 'passed');
  });

  after(async function () {
    await driver.quit();
    if (SAUCE_TESTING && driver) {
      await driver.sauceJobStatus(allPassed);
    }
  });

});
